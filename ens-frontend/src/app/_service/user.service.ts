import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserAuthService } from './user-auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = 'http://localhost:8585';

  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });

  constructor(private htppClient: HttpClient,
    private userAuthService: UserAuthService) { }

  login(loginData) {
    return this.htppClient.post(this.baseUrl + "/authenticate", loginData, { headers: this.requestHeader })
  }

  public roleMatch(allowedRoles): boolean {
    let isMatch = false;
    const userRoles: any = this.userAuthService.getRoles();

    if (userRoles != null && userRoles) {
      for (let i = 0; i < userRoles.length; i++) {
        for (let j = 0; j < allowedRoles.length; j++) {
          if (userRoles[i].roleName === allowedRoles[j]) {
            isMatch = true;
          } 
        }
      }
    }
    return isMatch;
  }
}
