import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-chat-message-out',
  templateUrl: './chat-message-out.component.html',
  styleUrls: ['./chat-message-out.component.css']
})
export class ChatMessageOutComponent implements OnInit {
  @Input()
  message: any;

  constructor() {
  }

  ngOnInit(): void {
    console.log('asdasdasdsad',this.message)
  }

}
