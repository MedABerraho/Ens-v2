import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string;
  password: string;
  error: string;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit() {
    const credentials = { username: this.username, password: this.password };
    this.userService.login(credentials).subscribe(
      (response: any) => {
        const token = response.token;
        localStorage.setItem('token', token);
        console.log('Login successful');
        this.router.navigate(['/home']);
      },
      error => {
        this.error = error.error.message;
        console.log('Login failed:', error);
      }
    );

  }
}