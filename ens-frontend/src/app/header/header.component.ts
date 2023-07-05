import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserAuthService } from '../_service/user-auth.service';
import { UserService } from '../_service/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(
    private userAuthService: UserAuthService,
    public userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  isLoggedIn() {
    return this.userAuthService.isLoggedIn()
  }

  logOut() {
    this.userAuthService.clear();
    this.router.navigate(['/home']);
  }

}
