import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/api.service';
import * as bcrypt from 'bcryptjs';
import { User } from 'src/models/user';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signUpForm: FormGroup;
  salt: string = '';
  user: any;
  isLoading: boolean;

  constructor(
    private apiService: ApiService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.isLoading = false;

    this.signUpForm = this.formBuilder.group({
      firstName: '',
      lastName: '',
      username: '',
      passHash: '',
      email: '',
      phone: '',

    })

   }


  ngOnInit(): void {
    this.salt = bcrypt.genSaltSync(16);
  }

  signUpSubmit(form: any){
    this.isLoading = true;
    console.log(form.value.username);

    form.value.passHash = bcrypt.hashSync(form.value.passHash, this.salt);

      // logic to validate username
      this.apiService.signUp(form.value).subscribe(res => {
          console.log(res)
          alert("Account Created Successfully. Please Login")
          this.isLoading = false;
          this.router.navigate(['login'])
      }, (error) => {
        this.isLoading = false;
        console.log("Duplicate Username")
        alert("Username is already taken")
      });
  }
}
