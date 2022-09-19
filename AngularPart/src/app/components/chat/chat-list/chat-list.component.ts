import {Component, OnChanges, OnInit} from '@angular/core';
import {MessageService} from "../../../service/message.service";

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent implements OnInit {
  clientConnected: boolean = false;

  activeChats: any[] = [];

  constructor(private messageService: MessageService) {
    this.messageService.serverActive.asObservable().subscribe((value) => {
      this.clientConnected = value
      if (this.clientConnected) {
        this.messageService.availableChats.asObservable().subscribe((chats) => {
          this.activeChats = chats;
        })
      }
    })
  }

  ngOnInit(): void {
  }

  deleteChat(chatId: string) {
    this.messageService.sendDeleteChatRequest(chatId);
  }
}

