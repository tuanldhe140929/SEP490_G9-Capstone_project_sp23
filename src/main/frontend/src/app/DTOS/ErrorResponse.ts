import { HttpStatusCode } from "@angular/common/http";

export class ErrorResponse {
  status: HttpStatusCode;
  messages: string[];
  timestamp: Date;
  constructor() {
    this.status = -1;
    this.messages = [];
    this.timestamp = new Date();
  }
}
