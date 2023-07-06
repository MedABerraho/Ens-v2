import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8585'; // Update with your backend API URL

  constructor(private httpClient: HttpClient) { }

  login(data: any) {
    return this.httpClient.post(this.baseUrl + "/user/login", data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    })
  }

  signup(data: any) {
    return this.httpClient.post(this.baseUrl + "/user/signup", data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    })
  }

  checkToken() {
    return this.httpClient.get(this.baseUrl + "/user/checkToken");
  }


}
