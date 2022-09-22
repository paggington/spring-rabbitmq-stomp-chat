import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MessageService} from "../../service/message.service";
import {ProfileService} from "../../service/profile.service";
import {ParticipantModel} from "../participant-creation/models/ParticipantModel";

@Component({
  selector: 'app-profile-holder',
  templateUrl: './profile-holder.component.html',
  styleUrls: ['./profile-holder.component.css']
})
export class ProfileHolderComponent implements OnInit {
  @Input()
  chatId: string = "";
  @Output()
  profileCreationEmitter: EventEmitter<ParticipantModel> = new EventEmitter<ParticipantModel>();
  userHaveProfile: boolean = false;

  constructor(private messageService: MessageService, private profileService: ProfileService) {
    this.profileService.currentProfile.asObservable().subscribe((profile) => {
      this.userHaveProfile = !profile;
    })
  }

  ngOnInit(): void {
  }

  emitProfileCreation(model: ParticipantModel) {
    if (model.nickname) {
      this.profileService.setProfile(model);
      if (this.profileService.currentProfile.value) {
        this.makeChatContainerVisible();
      }
    }
  }

  makeChatContainerVisible() {
    let currentChatContainer = document.getElementById(`chat-container-${this.chatId}`);
    let currentChatSwitchButton = document.getElementById(`switch-button-${this.chatId}`);
    if (currentChatContainer?.hasAttribute('hidden')) {
      currentChatSwitchButton!.textContent = 'Exit'
      currentChatContainer?.removeAttribute('hidden');
    } else {
      currentChatSwitchButton!.textContent = 'Join'
      currentChatContainer?.setAttribute('hidden', '');
    }
  }
}
