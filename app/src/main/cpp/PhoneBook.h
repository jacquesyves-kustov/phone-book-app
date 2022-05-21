#ifndef PHONEBOOKAPP_PHONEBOOK_H
#define PHONEBOOKAPP_PHONEBOOK_H

#include <string>
#include <map>
#include <vector>
#include "json.hpp"


using json = nlohmann::json;


// PhoneBook class declaration
class PhoneBook;


// Auxiliary class
class PhoneBookDestroyer {
private:
    PhoneBook* phoneBookInstance;
public:
    PhoneBookDestroyer();
    ~PhoneBookDestroyer();
    void initialize( PhoneBook* pt );
};


// PhoneBook class definition
class PhoneBook {
private:
    // Constructor is private
    PhoneBook();

    // Auxiliary class static instance
    static PhoneBookDestroyer destroyer;

    // Attributes
    static PhoneBook* ptInstance;
    std::map<std::string, std::string> contactMap;

public:
    static PhoneBook* getInstance();
    std::string sendContactsList() const;
    std::string findContactByName(const std::string& name) const;
    void addContact(const std::string& jsonStr);
};


#endif //PHONEBOOKAPP_PHONEBOOK_H
