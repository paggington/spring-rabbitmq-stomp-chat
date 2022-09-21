import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService} from "./service/message.service";
import {ProfileService} from "./service/profile.service";
import {ParticipantModel} from "./components/participant-creation/models/ParticipantModel";
import {migrateEntryComponentsUsages} from "@angular/core/schematics/migrations/entry-components/util";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'AngularPart';

  userHaveProfile: boolean = true;

  constructor(private messageService: MessageService, private profileService: ProfileService) {
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
}
