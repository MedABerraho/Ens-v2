import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-singup',
  templateUrl: './singup.component.html',
  styleUrls: ['./singup.component.css']
})
export class SingupComponent implements OnInit {

  username: string;
  password: string;
  contactNumber: string;
  address: string;
  error: any;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit() {
    const user = {
      username: this.username,
      password: this.password,
      address: this.address,
      contactNumber: this.contactNumber
    };
    this.userService.signup(user).subscribe(
      (response: any) => {
        // Handle the response from the backend
        console.log('Signup successful');
        this.router.navigate(['/home']);
      },
      error => {
        // Handle the error response from the backend
        this.error = error.error.message;
        console.log('Signup failed:', error);
      }
    );
  }

}
