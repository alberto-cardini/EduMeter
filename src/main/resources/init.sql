CREATE TABLE IF NOT EXISTS Users (
                                      id VARCHAR(255) PRIMARY KEY,
                                    banned BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Admin (
                                    id SERIAL PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL UNIQUE
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
                                    FOREIGN KEY (professor_id) REFERENCES Professor(id) ON DELETE CASCADE,
                                    UNIQUE (course_id, professor_id)
);

CREATE TABLE IF NOT EXISTS Drafted_Review (
                                    id SERIAL PRIMARY KEY,
                                    comment TEXT,
                                    date DATE,
                                    rating INT,
                                    difficulty INT,
                                    school VARCHAR(255),
                                    degree VARCHAR(255),
                                    course VARCHAR(255),
                                    professor VARCHAR(255),
                                    user_id VARCHAR(255) NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Published_Review (
                                    id SERIAL PRIMARY KEY,
                                    comment TEXT,
                                    date DATE NOT NULL,
                                    rating INT,
                                    difficulty INT,
                                    teaching_id INT NOT NULL,
                                    user_id VARCHAR(255) NOT NULL,
                                    FOREIGN KEY (teaching_id) REFERENCES Teaching(id) ON DELETE CASCADE,
                                    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Report (
                                    id SERIAL PRIMARY KEY,
                                    comment VARCHAR(500),
                                    date DATE NOT NULL,
                                    user_id VARCHAR(255) NOT NULL,
                                    review_id INT NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
                                    FOREIGN KEY (review_id) REFERENCES Published_Review(id) ON DELETE CASCADE
);