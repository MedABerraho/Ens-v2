import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {

  constructor() { }

  setRoles(roles: []) {
    localStorage.setItem("roles", JSON.stringify(roles));
  }

  getRoles(): [] {
    return JSON.parse(localStorage.getItem('roles')|| 'null' || '{}');
  }

  setToken(jwtToken: string) {
    localStorage.setItem("jwtToken", jwtToken);
  }

  getToken(): string {
    return JSON.stringify(localStorage.getItem("jwtToken"));
  }

  clear() {
    localStorage.clear();
  }

  isLoggedIn() {
    return this.getRoles() && this.getToken();
  }
}
