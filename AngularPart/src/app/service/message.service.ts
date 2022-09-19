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
        if (_this.stomp.status === "CONNECTED") {
          _this.stomp.subscribe('/topic/greetings', (greeting: any) => {
          });
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
    if (this.stomp !== null && this.stomp.status === "CONNECTED") {
      this.stomp.disconnect();
    }
    console.log("Disconnected");
  }
}
