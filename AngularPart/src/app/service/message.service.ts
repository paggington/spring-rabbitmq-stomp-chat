import {Injectable} from '@angular/core';
import SockJS from "sockjs-client";
import * as Stomp from 'stompjs';
import {BehaviorSubject, Observable, Observer} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  serverActive: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  availableChats: BehaviorSubject<any[]> = new BehaviorSubject<any[]>([]);

  private static SERVER_URL: string = "http://127.0.0.1:8080/ws";

  private socket: any;
  private stomp: any;

  constructor(private http: HttpClient) {
    this.connect()
  }

  public connect(): void {
    this.create();
    if (this.socket.status !== SockJS.OPEN) {
      this.stomp = Stomp.over(this.socket);
      const _this = this;
      _this.stomp.connect({}, function () {
        _this.serverActive.next(true);
        if (_this.socket.readyState === WebSocket.OPEN) {
          _this.fillChats();
          _this.subscribeOnChatCreateEvent();
          _this.subscribeOnChatDeleteEvent();
        }
        _this.stomp.reconnect_delay = 2000;
      }, () => {
        //error callback
      });
    }
  }

  private create(): void {
    this.socket = new SockJS(MessageService.SERVER_URL, {})
    let observable = new Observable((obs: Observer<MessageEvent>) => {
      this.socket.onmessage = obs.next.bind(obs);
      this.socket.onerror = obs.error.bind(obs);
      this.socket.onclose = obs.complete.bind(obs);
      return this.socket.close.bind(this.socket);
    });
    let observer = {
      error: null,
      complete: null,
      next: (data: Object) => {
        if (this.socket.readyState === WebSocket.OPEN) {
          this.socket.send(JSON.stringify(data));
        }
      }
    };
  }

  public disconnect() {
    if (this.stomp !== null) {
      this.stomp.disconnect();
    }
    console.log("Disconnected");
  }

  public sendCreateChatRequest(chatName: string) {
    this.stomp.send("/topic/chat.create", {}, chatName);
  }

  public sendDeleteChatRequest(chatId: string) {
    this.stomp.send("/topic/chat.delete", {},chatId);
  }

  public subscribeOnChatCreateEvent() {
    this.stomp.subscribe("/topic/chat.create.event", () => {
      this.fillChats();
    })
  }

  public subscribeOnChatDeleteEvent() {
    this.stomp.subscribe("/topic/chat.delete.event", () => {
      this.fillChats();
    })
  }

  public fillChats() {
    this.fetchActiveChats().subscribe((chats) => {
      this.availableChats.next(chats);
    })
  }

  public fetchActiveChats(): Observable<any> {
    return this.http.get<any>('http://localhost:8080/api/chats', {});
  }
}
