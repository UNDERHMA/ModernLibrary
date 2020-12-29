import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/api.service';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  books: any;
  isSearching: boolean;
  fname: string;
  user: any; 
  constructor(private apiService: ApiService, private router: Router) {
    this.isSearching = false;
    this.books = [];
    this.fname = '';
    if(localStorage['user']){
      this.user = JSON.parse(localStorage['user']);
    }
   }

  ngOnInit(): void {

    if(localStorage['user']){
      this.user = JSON.parse(localStorage['user']);
    } else {
      this.router.navigate(['login'])
    }

    this.fname = this.user.fname;

    this.isSearching = true;
    this.apiService.getBooksForWishlist(this.user.id).subscribe(res => {
      console.log(res);
      this.isSearching = false;
      this.books = res;

    }, err => {
      this.isSearching = false;
      console.log(err)

    })
  }

  addToCart(e: any){
    console.log(e)
    let image = e.path[2].children[0].currentSrc;
    let isbn = e.path[1].children[3].innerText
    let author = e.path[2].childNodes[1].childNodes[1].innerText
    let title = e.path[2].childNodes[1].childNodes[0].innerText
    let desc = e.path[1].childNodes[2].innerText
    
    let book: any = {"title": title, "author": author, "image": image, "isbn": isbn, "description": desc}
    console.log(book);
    
    this.apiService.addBookToCart(this.user.id, book).subscribe(res => {
        console.log('Book added to cart')
        // alert('Book added to cart'); 
        this.apiService.deleteFromWishlist(isbn, this.user.id).subscribe(res =>{ 
          let index;
          for(var i=0; i<this.books.length; i++){
            if (this.books[i].book.isbn == isbn){
              index = i;
            }
          }
          this.books.splice(index, 1);
        }, error => {
          console.log(error.error);
          alert(error.error);
        });
      }, error => {
        console.log(error.error);
        alert(error.error);
      });
  }

  emptyWishlist(e: any){

    this.apiService.deleteAllFromWishlist(this.user.id).subscribe(res =>{ 
      this.books = [];
    }, error => {
      console.log(error.error);
      // alert(error.error);
    });
  }

  deleteFromWishlist(e: any){
    console.log(e)
    let isbn = e.path[1].children[3].innerText
    
    this.apiService.deleteFromWishlist(isbn, this.user.id).subscribe(res =>{ 
      let index;
      for(var i=0; i<this.books.length; i++){
        if (this.books[i].book.isbn == isbn){
          index = i;
        }
      }
      this.books.splice(index, 1);
    }, error => {
      console.log(error.error);
      alert(error.error);
    });
  }

}
