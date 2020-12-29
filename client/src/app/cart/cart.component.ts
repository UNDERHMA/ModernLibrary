import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/api.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  books: any;
  isSearching: boolean;
  fname: string;
  user: any; 
  booksToSend: any;
  

  constructor(private apiService: ApiService, private router: Router) {
    this.isSearching = false;
    this.books = [];
    this.fname = '';
    this.booksToSend = [];
    

   }

  ngOnInit(): void {

    if(localStorage['user']){
      this.user = JSON.parse(localStorage['user']);
    } else {
      this.router.navigate(['login'])
    }

    this.fname = this.user.fname;

    this.isSearching = true;
    this.apiService.getBooksForCart(this.user.id).subscribe(res => {
      console.log(res);
      this.isSearching = false;
      this.books = res;
      
      for(let book of res){
        this.booksToSend.push(book.book);
      }
      console.log(this.booksToSend);

    }, err => {
      this.isSearching = false;
      console.log(err)

    })

  }
  checkOut(e: any){

    console.log(this.books);

    this.apiService.addBookAllReserved(this.user.id, this.booksToSend).subscribe(res => console.log(res), err => console.log(err))
    this.apiService.deleteAllFromCart(this.user.id).subscribe(res =>{ 
      this.books = [];
    }, error => {
      console.log(error.error);
      // alert(error.error);
    });
  }
  remove(e: any){
    console.log(e);
  
    let isbn = e.path[1].children[3].innerText;

    this.apiService.deleteFromCart(isbn, this.user.id).subscribe(res =>{ 
      let index;
      for(var i=0; i<this.books.length; i++){
        if (this.books[i].book.isbn == isbn){
          index = i;
        }
      }
      this.books.splice(index, 1);
    }, error => {
      console.log(error.error);
      // alert(error.error);
  });
  }
}
