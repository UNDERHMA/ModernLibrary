import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
import { Book } from '../../models/book';
import { of } from "rxjs";
import {
  debounceTime,
  map,
  distinctUntilChanged,
  filter
} from "rxjs/operators";
import { fromEvent } from 'rxjs';
import { HttpClient, HttpParams } from "@angular/common/http";
import { ApiService } from '../../api.service'
import { Router } from '@angular/router';

const PARAMS = new HttpParams({
  fromObject: {
    action: "opensearch",
    format: "json",
    origin: "*"
  }
});

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})

export class BooksComponent implements OnInit {

  @ViewChild('bookSearchInput', { static: true }) bookSearchInput!: ElementRef;
  cartName: string;
  wishName: string;
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
    this.cartName = 'Cart'
    this.wishName = 'WishList'

    if(localStorage['user']){
      this.user = JSON.parse(localStorage['user']);
    }

  }

  
  ngOnInit() {
    console.log(JSON.parse(localStorage['user']))
    console.log(this.user.id)
    
      fromEvent(this.bookSearchInput.nativeElement, 'keyup').pipe(

        // get value
        map((event: any) => {
          return event.target.value;
        })
        // if character length greater then 2
        , filter(res => res.length > 2)
  
        // Time in milliseconds between key events
        , debounceTime(500)
  
        // If previous query is diffent from current   
        , distinctUntilChanged()
  
        // subscription for response
      ).subscribe((text: string) => {
  
        this.isSearching = true;
  
        this.apiService.getBooks(text).subscribe((res) => {
          console.log('res', res);
          this.isSearching = false;
          this.apiResponse = res;
        }, (err) => {
          this.isSearching = false;
          console.log('error', err);
        });
  
      });
  }
  
  addToCart(e:any){
    
    console.log(e);
    
    if(!localStorage.getItem('user')){
      this.router.navigate(['login'])
    }
    
    let image = e.path[2].children[0].currentSrc;
    let isbn = e.path[1].children[3].innerText;
    let author = e.path[2].childNodes[1].childNodes[1].innerText
    let title = e.path[2].childNodes[1].childNodes[0].innerText
    let desc = e.path[1].childNodes[2].innerText
    
    let book: any = {"title": title, "author": author, "image": image, "isbn": isbn, "description": desc}
    console.log(book);
    console.log("id", this.user.id)
    this.apiService.addBookToCart(this.user.id, book).subscribe(
      res => {
      console.log('Book added to cart')
      alert('Book added to cart');
      // setTimeout(()=>{
      //   this.addedToCart = true;
      // }, 2000);
    }, error => {
        console.log(error.error);
        alert(error.error);
    });
      
      
  }; 
  
  addToWishlist(e:any){
   
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
   
    this.apiService.addBookToWishlist(this.user.id, book).subscribe(
      res => {
      console.log('Book added to wishlist')
      alert('Book added to wishlist');
      // setTimeout(()=>{
      //   this.addedToWishlist = true;
      // }, 2000);
    }, error => {
      console.log(error.error);
      alert(error.error);
    });
  }
}