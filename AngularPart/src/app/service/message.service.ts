import {Injectable} from '@angular/core';
import SockJS from "sockjs-client";
import * as Stomp from 'stompjs';
import {BehaviorSubject, Observable, Observer} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {randomFill} from "crypto";
import {round} from "@popperjs/core/lib/utils/math";
import {ParticipantModel} from "../components/participant-creation/models/ParticipantModel";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  serverActive: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  availableChats: BehaviorSubject<any[]> = new BehaviorSubject<any[]>([]);

  userSession: BehaviorSubject<string> = new BehaviorSubject<string>("");

  private static SERVER_URL: string = "http://127.0.0.1:8080/ws";

  private socket: any;
  private stomp: any;

  constructor(private http: HttpClient) {
    this.connect()
    this.userSession.next(this.socket.sessionId);
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
    this.socket = new SockJS(MessageService.SERVER_URL, {}, {
      sessionId: () => {
        let sessionId = round(Math.floor(Math.random() * 10000)).toFixed(12).toString().split(".")[0];
        this.userSession.next(sessionId)
        return sessionId;
      }
    })
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

  public sendMessage(message: string, chat_id: string, participantModel: ParticipantModel) {
    this.stomp.send(`/topic/chat.${chat_id}.messages.all.send`, {'simpSessionId': participantModel.sessionId}, message)
  }

  public subscribeOnChatMessages(chat_id: string) {
    this.stomp.subscribe(`/topic/chat.${chat_id}.messages`, (messages:any) => {
      console.log(messages)
    });
  }

  public sendCreateChatRequest(chatName: string) {
    this.stomp.send("/topic/chat.create", {}, chatName);
  }

  public sendDeleteChatRequest(chatId: string) {
    this.stomp.send("/topic/chat.delete", {}, chatId);
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

  public joinChat(chatId: string) {
    this.stomp.subscribe(`/topic/chat.${chatId}.participant.join`, () => {
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
