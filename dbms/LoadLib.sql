-- Mitchell Dees
-- Lab 7

SELECT 'LOADING Author' as 'INFO';
source load_authors.dump ;
SELECT 'LOADING Publisher' as 'INFO';
source load_publishers.dump ;
SELECT 'LOADING Book' as 'INFO';
source load_books.dump ;
SELECT 'LOADING Member' as 'INFO';
source load_members.dump ;
SELECT 'LOADING Phone' as 'INFO';
source load_phones.dump ;
SELECT 'LOADING WrittenBy' as 'INFO';
source load_written.dump ;
SELECT 'LOADING AuthorPhone' as 'INFO';
source load_aphone.dump ;
SELECT 'LOADING PublisherPhone' as 'INFO';
source load_pphone.dump ;
SELECT 'LOADING Library' as 'INFO';
source load_library.dump ;
SELECT 'LOADING LocatedAt' as 'INFO';
source load_located.dump ;
SELECT 'LOADING BorrowedBy' as 'INFO';
source load_borrowed.dump ;
