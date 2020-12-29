import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn: boolean = false; 
  
  constructor(private router: Router, public auth: AuthService) {
    this.router.onSameUrlNavigation = 'reload';
    // this.auth.isLoggedIn = false;
  }
  
  ngOnInit(): void {
    if(localStorage.getItem('user') != null){
      this.auth.isLoggedIn = true;
      // this.isLoggedIn = true;
    } else{
      this.auth.isLoggedIn = false;
      // this.isLoggedIn = false;
    }
  }
  checkUser(): void{
   
  }

}
