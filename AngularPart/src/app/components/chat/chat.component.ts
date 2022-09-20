import {Component, Input, OnInit} from '@angular/core';
import {MessageService} from "../../service/message.service";
import {ParticipantModel} from "../participant-creation/models/ParticipantModel";
import {ProfileService} from "../../service/profile.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  @Input()
  chat: any;

  chatHidden: boolean = false;

  currentUser: ParticipantModel | null = null;

  message: string = "";

  constructor(private messageService: MessageService, private profileService: ProfileService) {
    if (this.chat && this.chatHidden) {
      messageService.joinChat(this.chat.id);
    }
    if (this.profileService.currentProfile.value) {
      this.profileService.currentProfile.asObservable().subscribe((profile) => {
        this.currentUser = profile;
      })
    }
  }

  ngOnInit(): void {

  }

  changeHiddenState() {
    this.chatHidden = !this.chatHidden;
  }

  sendMessage() {
    if (this.currentUser) {
      this.messageService.sendMessage(this.message, this.chat.id, this.currentUser);
    }
  }
}
