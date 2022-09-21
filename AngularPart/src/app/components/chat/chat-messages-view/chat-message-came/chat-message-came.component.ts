import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-chat-message-came',
  templateUrl: './chat-message-came.component.html',
  styleUrls: ['./chat-message-came.component.css']
})
export class ChatMessageCameComponent implements OnInit {
  @Input()
  message: any;

  constructor() {
  }

  ngOnInit(): void {
  }

}
