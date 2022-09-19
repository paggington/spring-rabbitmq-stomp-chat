import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {ChatComponent} from './components/chat/chat.component';
import {MessageComponent} from './components/message/message.component';
import {ParticipantCreationComponent} from './components/participant-creation/participant-creation.component';
import {HttpClientModule} from "@angular/common/http";
import {ChatListComponent} from './components/chat/chat-list/chat-list.component';
import {ChatCreateComponent} from './components/chat/chat-create/chat-create.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    MessageComponent,
    ParticipantCreationComponent,
    ChatListComponent,
    ChatCreateComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
