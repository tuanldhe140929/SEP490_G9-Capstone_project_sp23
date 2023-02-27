import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Preview } from '../DTOS/Preview';

const baseUrl = "http://localhost:9000/preview"

@Injectable({
  providedIn: 'root'
})
export class PreviewService {

  constructor(private httpClient: HttpClient) { }

  uploadPreviewVideo(data: any): Observable<Preview> {
    return this.httpClient.post<Preview>(baseUrl + '/uploadPreviewVideo', data, {
      reportProgress: true
    });
  }

  uploadPreviewPicture(data: any): Observable<Preview[]> {
    return this.httpClient.post<Preview[]>(baseUrl + '/uploadPreviewPicture', data);
  }

  removePreviewVideo(productId: number, version: string) {
    return this.httpClient.delete(baseUrl + "/deletePreviewVideo", {
      params: {
        productId: productId,
        version: version
      }
    });
  }

  removePreviewPicture(previewId: number): Observable<Preview[]> {
    return this.httpClient.delete<Preview[]>(baseUrl + "/deletePreviewPicture", {
      params: {
        previewId: previewId
      }
    })
  }
}
