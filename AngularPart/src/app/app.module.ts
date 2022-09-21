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
import { ProfileHolderComponent } from './components/profile-holder/profile-holder.component';
import { ChatMessagesViewComponent } from './components/chat/chat-messages-view/chat-messages-view.component';
import { ChatMessageCameComponent } from './components/chat/chat-messages-view/chat-message-came/chat-message-came.component';
import { ChatMessageOutComponent } from './components/chat/chat-messages-view/chat-message-out/chat-message-out.component';

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    MessageComponent,
    ParticipantCreationComponent,
    ChatListComponent,
    ChatCreateComponent,
    ProfileHolderComponent,
    ChatMessagesViewComponent,
    ChatMessageCameComponent,
    ChatMessageOutComponent
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
