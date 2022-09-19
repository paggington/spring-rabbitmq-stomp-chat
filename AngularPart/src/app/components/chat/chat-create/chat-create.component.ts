import {Component, OnInit} from '@angular/core';
import {ChatInstanceModel} from "../models/ChatInstanceModel";
import {MessageService} from "../../../service/message.service";

@Component({
  selector: 'app-chat-create',
  templateUrl: './chat-create.component.html',
  styleUrls: ['./chat-create.component.css']
})
export class ChatCreateComponent implements OnInit {
  chatName: string = "";

  constructor(private messageService: MessageService) {
  }

  ngOnInit(): void {
  }

  sendChatCreateRequest() {
    if (this.chatName) {
      this.messageService.sendCreateChatRequest(this.chatName)
    }
    this.chatName = "";
  }
}
