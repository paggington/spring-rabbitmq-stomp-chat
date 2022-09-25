import {Injectable} from '@angular/core';
import SockJS from "sockjs-client";
import * as Stomp from 'stompjs';
import {BehaviorSubject, Observable, Observer, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {round} from "@popperjs/core/lib/utils/math";
import {ParticipantModel} from "../components/participant-creation/models/ParticipantModel";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  serverActive: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  availableChats: Subject<any[]> = new Subject<any[]>();

  userSession: BehaviorSubject<string> = new BehaviorSubject<string>("");

  cameMessage: Subject<any> = new Subject<any>();

  private static SERVER_URL: string = "http://127.0.0.1:8080/ws";

  private socket: any;
  public stomp: any;

  public subscribedOnChat: boolean = false;

  constructor(private http: HttpClient) {
    // HAHAHAHAH dude actually said "Hey lets call it multiple times lets see wats gonna be" he's committed suicide next day
    // in cause of two user sessions for one tab
    // this.connect()
  }

  public connect(): void {
    this.create();
    if (this.socket.sessionId) {
      this.userSession.next(this.socket.sessionId);
    }
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
        console.log('sessionId',sessionId)
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
  }

  public sendMessage(message: string, chat_id: string, participantModel: ParticipantModel, file: any) {
    this.stomp.send(`/topic/chat.${chat_id}.messages.all.send`, {
      'simpSessionId': participantModel.sessionId,
      'file': file
    }, message)
  }

  public subscribeOnChatMessages(chat_id: string) {
    this.subscribedOnChat = true;
    this.stomp.subscribe(`/topic/chat.${chat_id}.messages`, (message: any) => {
      if (message != null) {
        this.cameMessage.next(message);
      }
    });
  }

  public sendCreateChatRequest(chatName: string) {
    this.stomp.send("/topic/chat.create", {}, chatName);
  }

  public sendDeleteChatRequest(chatId: string) {
    this.stomp.send("/topic/chat.delete", {}, chatId).then(() => {
      this.stomp.unsubscribe();
    });
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
