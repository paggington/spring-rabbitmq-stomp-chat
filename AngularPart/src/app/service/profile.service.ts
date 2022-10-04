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

  public setProfile(userModel: ParticipantModel) {
    this.currentProfile.next(userModel);
    localStorage.setItem('profile', JSON.stringify(userModel))
  }

  public isUserProfileSet() {
    let profile = localStorage.getItem('profile')
    return profile !== 'undefined' && profile !== null
  }

  public loadUserProfile() {
    return localStorage.getItem('profile');
  }
}
