import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService} from "./service/message.service";
import {ProfileService} from "./service/profile.service";
import {environment} from "../environments/environment.prod";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'AngularPart';

  userHaveProfile: boolean = true;

  inRelease: boolean = environment.released;

  isUserProfileSet: boolean = this.checkIfUserHaveProfile();

  constructor(private messageService: MessageService,
              private profileService: ProfileService) {
  }

  public setProfileServiceUser(user: any) {
    if (user) {
      this.profileService.currentProfile.next(user);
    }
  }

  ngOnInit(): void {
    this.messageService.connect();

  }

  ngOnDestroy(): void {
    this.messageService.disconnect();
  }

  private checkIfUserHaveProfile() {
    return this.profileService.isUserProfileSet();
  }

  setUserProfile(userProfileValue: any) {
    this.profileService.setProfile(userProfileValue);
  }
}
