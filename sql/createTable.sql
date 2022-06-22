CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    author varchar(50) NOT NULL,
    title varchar(100) NOT NULL,
    genre varchar(20) NOT NULL,
    year varchar(5) NOT NULL,
    type varchar(20) NOT NULL
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username varchar(20) NOT NULL,
    password varchar(200) NOT NULL
);

CREATE TABLE favorites (
    favorite_id SERIAL PRIMARY KEY,
    user_id integer REFERENCES users (user_id) ON DELETE CASCADE,
    book_id integer REFERENCES books (id) ON DELETE CASCADE,
    favorited_at TIMESTAMP NOT NULL
);

CREATE TABLE reviews (
    review_id SERIAL PRIMARY KEY,
    user_id integer REFERENCES users (user_id) ON DELETE CASCADE,
    book_id integer REFERENCES books (id) ON DELETE CASCADE,
    review TEXT,
    rating integer NOT NULL,
    reviewed_at TIMESTAMP NOT NULL,
    username varchar(20) NOT NULL
);