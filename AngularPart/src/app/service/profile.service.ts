import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {ParticipantModel} from "../components/participant-creation/models/ParticipantModel";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  currentProfile: Subject<ParticipantModel> = new Subject<ParticipantModel>()

  constructor() {
  }

  public loadUserProfile() {
  }

  public setProfile(userModel: ParticipantModel) {
    this.currentProfile.next(userModel);
  }
}
