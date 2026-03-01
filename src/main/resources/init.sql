CREATE TABLE IF NOT EXISTS Users (
                                    id CHAR(22) PRIMARY KEY,
                                    banned BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Admin (
                                    id SERIAL PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Pin (
                                    id SERIAL PRIMARY KEY,
                                    pin VARCHAR(22) NOT NULL,
                                    user_id CHAR(22) NOT NULL,
                                    expires_at TIMESTAMP NOT NULL,
                                    isAdmin BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS School (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Professor (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL,
                                    surname VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Degree (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
                                    type VARCHAR(20) CHECK (type IN ('Bachelor', 'Master')),
                                    school_id INT NOT NULL,
                                    FOREIGN KEY (school_id) REFERENCES School(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Course (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
                                    degree_id INT NOT NULL,
                                    FOREIGN KEY (degree_id) REFERENCES Degree(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Teaching (
                                    id SERIAL PRIMARY KEY,
                                    course_id INT NOT NULL,
                                    professor_id INT NOT NULL,
                                    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE,
                                    FOREIGN KEY (professor_id) REFERENCES Professor(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Drafted_Review (
                                    id SERIAL PRIMARY KEY,
                                    user_id CHAR(22) NOT NULL,
                                    date DATE,
                                    school VARCHAR(255),
                                    degree VARCHAR(255),
                                    course VARCHAR(255),
                                    professor VARCHAR(255),
                                    enjoyment INT,
                                    difficulty INT,
                                    comment TEXT,
                                    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Published_Review (
                                    id SERIAL PRIMARY KEY,
                                    user_id CHAR(22) NOT NULL,
                                    date DATE NOT NULL,
                                    teaching_id INT,
                                    enjoyment INT,
                                    difficulty INT,
                                    comment TEXT,
                                    FOREIGN KEY (teaching_id) REFERENCES Teaching(id) ON DELETE CASCADE,
                                    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Up_vote (
                                    review_id INT NOT NULL,
                                    user_id CHAR(22) NOT NULL,
                                    FOREIGN KEY (review_id) REFERENCES Published_Review(id) ON DELETE CASCADE,
                                    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
                                    PRIMARY KEY (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS Report (
                                    id SERIAL PRIMARY KEY,
                                    comment VARCHAR(255),
                                    date DATE NOT NULL,
                                    user_id CHAR(22) NOT NULL,
                                    review_id INT NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
                                    FOREIGN KEY (review_id) REFERENCES Published_Review(id) ON DELETE CASCADE
);