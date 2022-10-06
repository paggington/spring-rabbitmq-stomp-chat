import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {ParticipantModel} from "../components/participant-creation/models/ParticipantModel";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  currentProfile: Subject<any> = new Subject<any>()

  constructor() {
    let currentLocalStorageProfile = this.loadUserProfile()
    if (currentLocalStorageProfile) {
      currentLocalStorageProfile = JSON.parse(currentLocalStorageProfile)
      // @ts-ignore
      this.currentProfile.next(currentLocalStorageProfile);
    }
    this.currentProfile.asObservable().subscribe(user => {
      localStorage.setItem('profile', JSON.stringify(user))
    })
  }

  public setSessionIdForUser(sessionId: any) {
    let currentUserProfile = this.loadUserProfile()
    if (currentUserProfile) {
      currentUserProfile = JSON.parse(currentUserProfile);
      // @ts-ignore
      currentUserProfile.sessionId = sessionId
      this.currentProfile.next(currentUserProfile)
    }
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
