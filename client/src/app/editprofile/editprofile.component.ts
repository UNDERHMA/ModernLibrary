import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from 'src/api.service';
import { User } from 'src/models/user';
import * as bcrypt from 'bcryptjs';

@Component({
  selector: 'app-editprofile',
  templateUrl: './editprofile.component.html',
  styleUrls: ['./editprofile.component.css']
})
export class EditprofileComponent implements OnInit {

  editProfile: FormGroup;
  user: any;
  userId: number = 0;
  salt: string = '';

  constructor(
    private apiService: ApiService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    if(localStorage['user']){
      this.user = JSON.parse(localStorage['user']);
      } 
    this.editProfile = this.formBuilder.group({
      firstName: this.user.fname,
      lastName: this.user.lname,
      username: this.user.username,
      passHash: '',
      email: this.user.email,
      phone: this.user.phone,
    })
    
   }

  ngOnInit(): void {
    this.user = JSON.parse(localStorage['user'])
    console.log(this.user)
    this.userId = this.user.id;
    console.log(this.user.id)
    this.salt = bcrypt.genSaltSync(16);
  }
  editProfileSubmit(form: any){
    console.log(form.value); 
    console.log(this.userId);
    for(var prop in form.value) {
      if(form.value[prop] === '') {
        alert("Please fill in all fields");
        return;
      }
    }
    let memSince = this.user.memSince;
    
    form.value.passHash = bcrypt.hashSync(form.value.passHash, this.salt);
    
    this.apiService.editProfile(form.value,this.user.id).subscribe(res=>{
      console.log(res)
  }, error => console.log(error));
    this.user =new User(this.userId, form.value.firstName, form.value.lastName, form.value.username, form.value.email, form.value.phone, memSince)
    localStorage.setItem("user", JSON.stringify(this.user));

    this.router.navigate(['profile'])
}
}