import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {ParticipantModel} from "../components/participant-creation/models/ParticipantModel";
import {MessageService} from "./message.service";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  currentProfile: BehaviorSubject<ParticipantModel> = new BehaviorSubject<ParticipantModel>({
    sessionId: undefined,
    id: undefined,
    chatId: undefined,
    nickname: undefined,
    enteredAt: undefined
  })

  constructor(private messagingService: MessageService) {
  }

  public loadUserProfile() {

  }

  public setProfile(userModel:ParticipantModel){
    console.log('usernmae',userModel);
    this.currentProfile.next(userModel);
  }
}
