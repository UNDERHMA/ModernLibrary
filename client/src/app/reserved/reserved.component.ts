import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/api.service';

@Component({
  selector: 'app-reserved',
  templateUrl: './reserved.component.html',
  styleUrls: ['./reserved.component.css']
})
export class ReservedComponent implements OnInit {
  books: any;
  isSearching: boolean;
  fname: string;
  user: any;

  constructor(private apiService: ApiService, private router: Router) { 
    this.isSearching = false;
    this.books = [];
    this.fname = '';
    

  }

  ngOnInit(): void {

    if(localStorage['user']){
      this.user = JSON.parse(localStorage['user']);
    } else {
      this.router.navigate(['login'])
    }

    this.fname = this.user.fname;

    this.isSearching = true;
    this.apiService.getBooksForReserved(this.user.id).subscribe(res => {
      console.log(res);
      this.isSearching = false;
      this.books = res;

    }, err => {
      this.isSearching = false;
      console.log(err)

    })
  }

  returnOne(e:any){

    let isbn = e.path[1].children[3].innerText;

    let index;
    for(var i=0; i<this.books.length; i++){
      if (this.books[i].book.isbn == isbn){
        index = i;
      }
    }
    this.books.splice(index, 1);

      this.apiService.deleteFromReserved(isbn, this.user.id).subscribe(res =>{
     }, error => {
        console.log(error.error);
        alert(error.error);
    });
  }

  returnBooks(e:any){
    this.apiService.deleteAllFromReserved(this.user.id).subscribe(res =>{
       console.log(res)
      //  alert(res);
       this.books = [];
      }, error => {
        console.log(error.error);
        alert(error.error);
    });
  }

}
