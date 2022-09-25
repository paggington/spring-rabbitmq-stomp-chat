import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {ParticipantModel} from "./models/ParticipantModel";
import {MessageService} from "../../service/message.service";
import {adjustElementAccessExports} from "@angular/compiler-cli/ngcc/src/packages/adjust_cjs_umd_exports";

@Component({
  selector: 'app-participant-creation',
  templateUrl: './participant-creation.component.html',
  styleUrls: ['./participant-creation.component.css']
})
export class ParticipantCreationComponent implements OnInit, OnDestroy {
  @Output()
  createSessionProfile: EventEmitter<ParticipantModel> = new EventEmitter<ParticipantModel>();

  nickname: string = "";
  @Input()
  chatId: string = "";

  sessionId: any;

  private model: ParticipantModel = {
    id: undefined,
    enteredAt: undefined,
    sessionId: undefined,
    chatId: undefined,
    nickname: undefined
  }

  constructor(private messagingService: MessageService) {
    this.messagingService.userSession.asObservable().subscribe((sessionId: any) => {
      this.model.sessionId = sessionId;
    })
  }

  ngOnDestroy(): void {
    this.messagingService.userSession.unsubscribe()
  }

  ngOnInit(): void {
  }

  createWithProvidedName() {
    this.model.chatId = this.chatId;
    this.model.nickname = this.nickname;
    if (this.model.nickname && this.model.sessionId != null) {
      this.createSessionProfile.emit(this.model);
    }
  }
}
