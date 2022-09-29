import {Component, Input, OnInit} from '@angular/core';
import {MessageService} from "../../../../service/message.service";
import {HttpEvent, HttpEventType} from "@angular/common/http";

@Component({
  selector: 'app-chat-message-out',
  templateUrl: './chat-message-out.component.html',
  styleUrls: ['./chat-message-out.component.css']
})
export class ChatMessageOutComponent implements OnInit {
  @Input()
  message: any;

  imageSpaceVisible: boolean = false;

  messageImage: any = null;

  messagedContentStatus: { status: string, requestType: string, percent: number } = {
    status: 'inactive',
    requestType: 'none',
    percent: 0
  };

  constructor(private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.imageSpaceVisible = this.message.haveByteContent;
    this.fetchMessageImage()
  }

  fetchMessageImage() {
    if (this.message) {
      if (this.message.haveByteContent) {
        this.messageService.fetchMessageByteContent(this.message.id).subscribe({
          next: (image) => {
            this.reportProgress(image)
          }
        })
      }
    }
  }

  private reportProgress(httpEvent: HttpEvent<string[] | Blob>) {
    switch (httpEvent.type) {
      case HttpEventType.UploadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Uploading');
        break;
      case HttpEventType.DownloadProgress:
        this.updateStatus(httpEvent.loaded, httpEvent.total!, 'Downloading');
        break;
      case HttpEventType.Response:
        if (httpEvent.body) {
          this.messageImage = `data:image/jpeg;base64,${httpEvent.body}`
        }
        this.messagedContentStatus.status = 'done';
        break;
      default:
        break;
    }
  }

  private updateStatus(loaded: number, total: number, type: string) {
    this.messagedContentStatus.status = 'progress';
    this.messagedContentStatus.requestType = type;
    this.messagedContentStatus.percent = Math.round(100 * loaded / total);
  }
}
