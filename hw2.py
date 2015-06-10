# w4tk.py  basic menues
from tkinter import *

class openWindow(Frame):
    def __init__(self, parent):
        Frame.__init__(self, parent)   
        self.parent = parent        
        self.initUI()

    def initUI(self):
        self.parent.title("Simple menu")
        self.parent.geometry("100x100+300+300")
        menubar = Menu(self.parent)
        self.parent.config(menu=menubar)
        self.parent.bind("<Key>", self.callback)
        
        fileMenu = Menu(menubar)
        menubar.add_cascade(label="File", menu=fileMenu)

        self.e = Entry(self.parent)
        self.e.pack()
        self.e.focus_set()

        b1 = Button(self.parent, text="OK", width=7, command=self.callback)
        b1.pack()
        b2 = Button(self.parent, text="Cancel", width=7, command=self.cancel)
        b2.pack()

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
        self.quit()
        
def main():
    data_entry = Window(Tk())
    data_entry.mainloop()  

main()  
