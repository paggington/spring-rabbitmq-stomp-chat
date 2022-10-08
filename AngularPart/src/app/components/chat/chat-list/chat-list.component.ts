import {Component, OnInit} from '@angular/core';
import {MessageService} from "../../../service/message.service";
import {ChatHolderService} from "../../../service/chat-holder.service";
import {ProfileService} from "../../../service/profile.service";

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.css']
})
export class ChatListComponent implements OnInit {
  clientConnected: boolean = false;

  activeChats: any[] = [];

  chatOpened: boolean = false;

  private currentUserProfile: any;

  constructor(private messageService: MessageService,
              private chatHolderService: ChatHolderService,
              private profileService: ProfileService) {
  }

  ngOnInit(): void {
    this.init()
  }

  init() {
    this.messageService.serverActive.asObservable().subscribe((value) => {
      this.clientConnected = value
      if (this.clientConnected) {
        this.messageService.availableChats.asObservable().subscribe((chats) => {
          this.activeChats = chats;
        })
        this.profileService.currentProfile.asObservable().subscribe(user => {
          if (user) {
            this.currentUserProfile = user;
          }
        })
      }
    })
  }

  deleteChat(chatId: string) {
    this.messageService.sendDeleteChatRequest(chatId);
  }

  setPickedChat(chat: any) {
    if (chat) {
      this.chatHolderService.changeCurrentChat(chat);
      this.setChatVisibility(chat)
      this.subscribeUserOnChat(chat)
    }
  }

  exitChat() {
    this.chatHolderService.pickedChat.complete();
  }

  subscribeUserOnChat(chat: any) {
    this.messageService.subscribeOnChatMessages(chat.id);
  }

  setChatVisibility(chat: any) {
    if (chat.id) {
      let chatContainer = document.getElementById(`chat-container-${chat.id}`)
      if (chatContainer != null) {
        if (this.chatOpened) {
          for (let activeChat of this.activeChats) {
            let activeChatContainer = document.getElementById(`chat-container-${activeChat.id}`)
            if (activeChatContainer != null) {
              activeChatContainer.setAttribute('hidden', 'true')
            }
          }

          this.chatOpened = false;
        } else {
          chatContainer.removeAttribute('hidden');
          this.chatOpened = true;
        }
      }
    }
  }
}

