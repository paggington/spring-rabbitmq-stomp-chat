import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ParticipantModel} from "./models/ParticipantModel";
import {MessageService} from "../../service/message.service";

@Component({
  selector: 'app-participant-creation',
  templateUrl: './participant-creation.component.html',
  styleUrls: ['./participant-creation.component.css']
})
export class ParticipantCreationComponent implements OnInit {
  @Output()
  createSessionProfile: EventEmitter<ParticipantModel> = new EventEmitter<ParticipantModel>();

  nickname: string = "";
  @Input()
  chatId: string = "";

  sessionId: string = "";

  constructor(private messagingService: MessageService) {
    this.messagingService.userSession.asObservable().subscribe((sessionId) => {
      this.sessionId = sessionId;
    })
  }

  ngOnInit(): void {
  }

  createWithProvidedName() {
    if (this.nickname != "") {
      let model: ParticipantModel = {
        id: undefined,
        enteredAt: undefined,
        sessionId: this.sessionId,
        chatId: this.chatId,
        nickname: this.nickname
      }

      this.createSessionProfile.emit(model);
    }
  }
}
