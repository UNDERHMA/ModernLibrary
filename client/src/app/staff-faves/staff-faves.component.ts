import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/api.service';

@Component({
  selector: 'app-staff-faves',
  templateUrl: './staff-faves.component.html',
  styleUrls: ['./staff-faves.component.css']
})
export class StaffFavesComponent implements OnInit {
  apiResponse: any;
  isSearching: boolean;
  addedToCart: boolean;
  addedToWishlist: boolean;
  user: any;

  constructor( private http: HttpClient, private apiService: ApiService, private router: Router ) { 
    this.isSearching = false;
    this.addedToCart = false;
    this.addedToWishlist = false;
    this.apiResponse = [];

    if(localStorage['user']){
      this.user = JSON.parse(localStorage['user']);
    }

  }
  ngOnInit(): void {
    
  }

  addToCart(e:any){
    console.log(e);
    
    if(!localStorage.getItem('user')){
      this.router.navigate(['login'])
    }
    
    let image = e.path[2].children[0].currentSrc;
    let isbn = e.path[1].children[5].innerText;
    let author = e.path[2].childNodes[1].childNodes[1].innerText
    let title = e.path[2].childNodes[1].childNodes[0].innerText
    let desc = e.path[1].childNodes[2].innerText
    
    let book: any = {"title": title, "author": author, "image": image, "isbn": isbn, "description": desc}
    console.log(book);
    
    this.apiService.addBookToCart(this.user.id, book).subscribe(res => {
      console.log(res)
      setTimeout(()=>{
        this.addedToCart = true;
      }, 2000);
    }, error => console.log(error));
      
      
  }; 
  
  addToWishlist(e:any){
    this.addedToWishlist = true;
    console.log(e);
    if(!localStorage.getItem('user')){
      this.router.navigate(['login'])
    }
    let image = e.path[2].children[0].currentSrc;
    let isbn = e.path[1].children[3].innerText
    let author = e.path[2].childNodes[1].childNodes[1].innerText
    let title = e.path[2].childNodes[1].childNodes[0].innerText
    let desc = e.path[1].childNodes[2].innerText
    
    let book: any = {"title": title, "author": author, "image": image, "isbn": isbn, "description": desc}
    console.log(book);
   
    this.apiService.addBookToWishlist(this.user.id, book).subscribe(res => {
      console.log(res)
      setTimeout(()=>{
        this.addedToWishlist = true;
      }, 2000);
    }, error => console.log(error));
  }
}
