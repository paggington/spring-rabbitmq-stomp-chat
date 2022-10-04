import {Injectable} from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChatHolderService {
  pickedChat: Subject<any> = new Subject<any>();

  constructor() {
  }

  changeCurrentChat(chat:any){
    if(chat.id){
      this.pickedChat.next(chat)
    }
  }
}
