import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService} from "./service/message.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'AngularPart';

  constructor(private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.messageService.connect();
  }

  ngOnDestroy(): void {
    this.messageService.disconnect();
  }
}
