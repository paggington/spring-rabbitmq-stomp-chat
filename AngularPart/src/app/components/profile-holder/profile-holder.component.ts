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
    this.profileService.setProfile(model);
    this.profileCreationEmitter.emit(model);
  }
}
