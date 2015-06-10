#CMSC 437 GUI Programming
#HW2
#Simple menu that reads a user-specified text file

from tkinter import *

class openWindow(Frame):
    def __init__(self, parent):
        Frame.__init__(self, parent)   
        self.parent = parent        
        self.initUI()

    def initUI(self):
        self.parent.title("Simple menu")
        self.parent.geometry("100x60+350+350")
        menubar = Menu(self.parent)
        self.parent.config(menu=menubar)
        #bind calls enter() with 2 args
        # so can't use self.callback
        self.parent.bind("<Return>", self.enter)

        self.e = Entry(self.parent)
        self.e.pack()
        self.e.focus_set()

        b1 = Button(self.parent, text="OK", width=7, command=self.callback)
        b1.pack()
        b2 = Button(self.parent, text="Cancel", width=7, command=self.cancel)
        b2.pack()

    def enter(self, key):
        self.callback()

    def callback(self):
        filename = self.e.get()
        if filename != '':
            with open(filename) as f:
                content = f.readlines()

            for line in content:
                print(line, end='')
        self.parent.destroy()

    def cancel(self):
        self.parent.destroy()

class Window(Frame):
    def __init__(self, parent):
        Frame.__init__(self, parent)   
        self.parent = parent        
        self.initUI()
        
    def initUI(self):
        self.parent.title("Menu")
        self.parent.geometry("200x200+300+300")
        menubar = Menu(self.parent)
        self.parent.config(menu=menubar)
        
        fileMenu = Menu(menubar)
        fileMenu.add_command(label="Open", command=self.onOpen)
        fileMenu.add_command(label="Exit", command=self.onExit)
        menubar.add_cascade(label="File", menu=fileMenu)

    def onOpen(self):
        frame = Tk()
        open_file_win = openWindow(frame)
        open_file_win.mainloop()

    def onExit(self):
        self.parent.destroy()
        
def main():
    data_entry = Window(Tk())
    data_entry.mainloop()  

main()  
