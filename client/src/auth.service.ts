import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isLoggedIn:boolean = false;
  constructor() { }

  public signIn(){
    this.isLoggedIn = true; 
  }
  public signOut(){
    this.isLoggedIn = false;
  }
}
