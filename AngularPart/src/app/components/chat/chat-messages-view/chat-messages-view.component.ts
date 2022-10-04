import {Component, Input, OnInit} from '@angular/core';
import {ProfileService} from "../../../service/profile.service";
import {MessageService} from "../../../service/message.service";
import {ParticipantModel} from "../../participant-creation/models/ParticipantModel";

@Component({
  selector: 'app-chat-messages-view',
  templateUrl: './chat-messages-view.component.html',
  styleUrls: ['./chat-messages-view.component.css']
})
export class ChatMessagesViewComponent implements OnInit {

  @Input()
  currentChat: any;

  currentUser: ParticipantModel | null = null;

  messages: any[] = [];

  previousMessage: any;

  constructor(private messageService: MessageService, private profileService: ProfileService) {
    this.profileService.currentProfile.asObservable().subscribe((profile) => {
      this.currentUser = profile;
      if (!this.messageService.subscribedOnChat) {
        this.messageService.subscribeOnChatMessages(this.currentChat.id)
      }
    })
    this.messageService.cameMessage.asObservable().subscribe((message) => {
      this.messages.push(JSON.parse(message.body));
    })
  }

  ngOnInit(): void {
  }

}
