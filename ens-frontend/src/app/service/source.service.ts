import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Source } from '../model/source';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SourceService {

  private baseUrl = "http://localhost:8585";
  constructor(private httpClient: HttpClient) { }

  addSource(source: Source): Observable<Object> {
    return this.httpClient.post(`${this.baseUrl}` + "/sources/add", source);
  }

  getSources(): Observable<Source[]> {
    return this.httpClient.get<Source[]>(`${this.baseUrl}` + "/sources");
  }

  updateSource(id: number, source: Source): Observable<Object> {
    return this.httpClient.post(`${this.baseUrl}` + "/sources/update/" + `{id}`, source);
  }

  deleteSource(id: number, source: Source): Observable<Object> {
    return this.httpClient.post(`${this.baseUrl}` + "/sources/delete/" + `{id}`, source);
  }
}
