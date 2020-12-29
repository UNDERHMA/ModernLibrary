import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  fname:string = '';
  lname: string = '';
  username: string = '';
  email: string = '';
  phone: string = '';
  memSince: any;

  user: any;
  constructor() {
    if(localStorage['user']){
    this.user = JSON.parse(localStorage['user']);
    } 
  }

  ngOnInit(): void {
    
    console.log(this.user)
    this.fname = this.user.fname;
    this.lname = this.user.lname;
    this.username = this.user.username;
    this.email = this.user.email;
    this.phone = this.user.phone;
    this.memSince = this.user.memSince;

  }

}
