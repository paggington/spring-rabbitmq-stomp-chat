import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {ChatComponent} from './components/chat/chat.component';
import {MessageComponent} from './components/message/message.component';
import {ParticipantCreationComponent} from './components/participant-creation/participant-creation.component';
import {HttpClientModule} from "@angular/common/http";
import { ChatListComponent } from './components/chat-list/chat-list.component';

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    MessageComponent,
    ParticipantCreationComponent,
    ChatListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
