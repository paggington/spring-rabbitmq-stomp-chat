import {Component, Input, OnInit} from '@angular/core';
import {ProfileService} from "../../../service/profile.service";
import {MessageService} from "../../../service/message.service";
import {ParticipantModel} from "../../participant-creation/models/ParticipantModel";
import {using} from "rxjs";
import {log} from "util";

@Component({
  selector: 'app-chat-messages-view',
  templateUrl: './chat-messages-view.component.html',
  styleUrls: ['./chat-messages-view.component.css']
})
export class ChatMessagesViewComponent implements OnInit {

  currentUser: ParticipantModel | null = null;

  constructor(private messageService: MessageService, private profileService: ProfileService) {
    this.profileService.currentProfile.asObservable().subscribe((profile) => {
      this.currentUser = profile;
      if (this.currentUser && this.currentUser.chatId) {
        this.messageService.subscribeOnChatMessages(this.currentUser.chatId)
      }
    })

  }

  ngOnInit(): void {
  }

}
