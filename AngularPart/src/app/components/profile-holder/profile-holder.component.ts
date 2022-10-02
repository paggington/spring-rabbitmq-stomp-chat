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
  chat: any;
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
      if (this.profileService.currentProfile) {
        if (this.chat) {
          this.makeChatContainerVisible();
        }
      }
    }
  }

  makeChatContainerVisible() {
    let currentChatContainer = document.getElementById(`chat-container-${this.chat.id}`);
    let currentChatSwitchButton = document.getElementById(`switch-button-${this.chat.id}`);
    if (currentChatContainer?.hasAttribute('hidden')) {
      currentChatSwitchButton!.textContent = 'Exit'

      currentChatContainer?.removeAttribute('data-toggle')
      currentChatContainer?.removeAttribute('data-target')
      currentChatContainer?.removeAttribute('hidden');
    } else {
      currentChatSwitchButton!.textContent = 'Join'

      currentChatContainer?.setAttribute('hidden', '');
      currentChatContainer?.setAttribute('data-toggle', 'modal')
      currentChatContainer?.setAttribute('data-target', '#profileModal')
    }
  }
}
