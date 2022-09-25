import {Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from "rxjs";
import {ParticipantModel} from "../components/participant-creation/models/ParticipantModel";
import {MessageService} from "./message.service";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  currentProfile: Subject<ParticipantModel> = new Subject<ParticipantModel>()

  constructor(private messagingService: MessageService) {
  }

  public loadUserProfile() {

  }

  public setProfile(userModel:ParticipantModel){
    console.log('USER MODEL',userModel)
    this.currentProfile.next(userModel);
  }
}
