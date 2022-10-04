import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {ParticipantModel} from "./models/ParticipantModel";
import {MessageService} from "../../service/message.service";

@Component({
  selector: 'app-participant-creation',
  templateUrl: './participant-creation.component.html',
  styleUrls: ['./participant-creation.component.css']
})
export class ParticipantCreationComponent implements OnInit, OnDestroy {
  @Output()
  createSessionProfile: EventEmitter<ParticipantModel> = new EventEmitter<ParticipantModel>();

  nickname: string = "";

  sessionId: any;

  private model: ParticipantModel = {
    id: undefined,
    enteredAt: undefined,
    sessionId: undefined,
    nickname: undefined
  }

  constructor(private messagingService: MessageService) {
    this.messagingService.userSession.asObservable().subscribe((sessionId: any) => {
      this.model.sessionId = sessionId;
    })
  }

  ngOnDestroy(): void {
  }

  ngOnInit(): void {
  }

  createWithProvidedName() {
    this.model.nickname = this.nickname;
    if (this.model.nickname && this.model.sessionId != null) {
      this.createSessionProfile.emit(this.model);
    }
  }
}
