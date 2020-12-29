import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/api.service';
import { User } from 'src/models/user';
import * as bcrypt from 'bcryptjs';
import { Router } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { AuthService } from 'src/auth.service';

@Component({
  providers: [NavbarComponent],
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string ='';
  password: string ='';
  salt: string ='';
  user: any;
  isLoading: boolean;

  constructor( private apiService: ApiService, private router: Router, private nav: NavbarComponent, private auth: AuthService) {
    this.isLoading = false;
  }

  ngOnInit(): void {}

  loginSubmit(){
    this.isLoading = true;
    let obj: any =  this.apiService.getUser(this.username).subscribe(
       res=> {
         console.log(res);
         
        if(bcrypt.compareSync(this.password, res.passHash)){
          this.user =new User(res.id, res.firstName, res.lastName, res.username, res.email, res.phone, res.registrationDate)

          localStorage.setItem("user", JSON.stringify(this.user));
          console.log(this.user)
          this.isLoading = false;
          this.auth.signIn();
          this.router.navigate(['home'])

        }else{
          alert("Passwords did not match")
          this.router.navigate(['cart'])
        }
        
       },(error) => {
        this.isLoading = false;
        console.log("Invalid Username/Password")
        alert("Invalid Username or Password")
      })
  }

}
